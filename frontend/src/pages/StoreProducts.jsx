import {useEffect, useState} from "react";
import {useAuth} from "../provider/authProvider";
import axios from "axios";
import * as React from "react";
import TableContainer from "@mui/material/TableContainer";
import Card from "@mui/material/Card";
import Table from "@mui/material/Table";
import TableHead from "@mui/material/TableHead";
import StyledTableRow from "../styledComponent/styledTableRow";
import StyledTableCell from "../styledComponent/styledTableCell";
import TableBody from "@mui/material/TableBody";
import TableRow from "@mui/material/TableRow";
import {
    Box,
    Button,
    Dialog,
    DialogContent,
    FormControl,
    IconButton, MenuItem,
    Stack,
    Typography
} from "@mui/material";
import KeyboardArrowUpIcon from "@mui/icons-material/KeyboardArrowUp";
import KeyboardArrowDownIcon from "@mui/icons-material/KeyboardArrowDown";
import ModeEditIcon from "@mui/icons-material/ModeEdit";
import DeleteOutlineOutlinedIcon from "@mui/icons-material/DeleteOutlineOutlined";
import Collapse from "@mui/material/Collapse";
import PercentIcon from '@mui/icons-material/Percent';
import {Container} from "react-bootstrap";
import Alert from "@mui/material/Alert";
import Autocomplete from '@mui/material/Autocomplete';
import StyledTextField from "../styledComponent/styledTextField";
import StyledButton from "../styledComponent/styldButton";
import AddIcon from "@mui/icons-material/Add";
import SearchContainer from "../styledComponent/searchContainer";
import SearchIconWrapper from "../styledComponent/searchIconWrapper";
import SearchIcon from "@mui/icons-material/Search";
import StyledInputBase from "../styledComponent/styledInputBase";


import StyledLabel from "../styledComponent/styldLabel";
import StyledSelect from "../styledComponent/styledSelect";

const StoreProducts = () => {
    const [storeProducts, setStoreProducts] = useState([])
    const [products, setProducts] = useState([])
    const {role} = useAuth()
    const [errorMessage, setErrorMessage] = useState("");
    const [openForm, setOpenForm] = useState(false)
    const [updateRow, setUpdateRow] = useState(undefined)
    const [cashierSearch, setCashierSearch] = useState("")
    const [UPCSearch, setUPCSearch] = useState("")
    const [storeProductFilter, setStoreProductFilter] = useState(0);

    const fetchStoreProductsData = async () => {
        let response
        if (UPCSearch > 0 && !isNaN(UPCSearch)){
            await fetchSearchingStoreProductsDataUPC(UPCSearch)
            return;
        }
        else {
            if (storeProductFilter === 0) {
                if (role === "MANAGER")
                    response = await axios.get("http://localhost:8080/api/store-product/order-by/count");
                else
                    response = await axios.get("http://localhost:8080/api/store-product/order-by/name")
            } else {
                response = await axios.get("http://localhost:8080/api/store-product/order-by/count/name?promotional=" + (storeProductFilter === 1))
            }
        }
        console.log(response)
        setStoreProducts(response.data)
    }

    const fetchProductsData = async () => {
        const response = await axios.get("http://localhost:8080/api/product/without/storeproduct")
        setProducts(response.data)
    };

    const fetchSearchingStoreProductsDataCashier = async (product) => {
        const response = await axios.get("http://localhost:8080/api/store-product/by-product?productName=" + product)
        setStoreProducts(response.data);
    };

    const fetchSearchingStoreProductsDataUPC = async (upc) => {
        const response = await axios.get("http://localhost:8080/api/store-product/by-upc/" + upc)
        if (response.data.error)
            setStoreProducts([]);
        else
            setStoreProducts(response.data);
    };

    useEffect(() => {
        fetchStoreProductsData()
    }, [storeProductFilter, UPCSearch])

    function processStoreProducts(storeProduct) {
        return {
            id: storeProduct.id,
            productId: storeProduct.product.id,
            name: storeProduct.product.name,
            sellingPrice: storeProduct.sellingPrice,
            productsNumber: storeProduct.productsNumber,
            characteristics: storeProduct.product.characteristics,
            promotional: storeProduct.promotional
        }
    }

    const columns = [
        {field: 'upc', headerName: 'UPC'},
        {field: 'product', headerName: 'Product'},
        {field: 'sellingPrice', headerName: 'Price'},
        {field: 'productsNumber', headerName: 'Number'}
    ];

    const rows = storeProducts.map((storeProduct) => processStoreProducts(storeProduct))

    const clear = e => {
        setErrorMessage("")
    };

    async function handleOpenForm() {
        setOpenForm(true)
        await fetchProductsData()
    }

    async function handleUpdate(row) {
        setUpdateRow(row)
        await handleOpenForm()
    }

    function handleClose(){
        setOpenForm(false)
        setUpdateRow(undefined)
    }

    const handleDelete = async (storeProductId) => {
        const response = await axios.delete("http://localhost:8080/api/store-product/" + storeProductId)
        if (response.data.error) {
            setErrorMessage("Can't delete store product that is present in at least one check")
            setTimeout(() => setErrorMessage(""), 3500)
        } else {
            if (response.data === true)
                await fetchStoreProductsData()
        }
    }

    async function handlePromotional(row) {
        let response;
        if (row.promotional) {
            response = await axios.put("http://localhost:8080/api/store-product/unpromotion/" + row.id);
        }
        else {
            response = await axios.put("http://localhost:8080/api/store-product/promotion/" + row.id)
        }
        if (response.data.error) {
            setErrorMessage(response.data.error)
            setTimeout(() => setErrorMessage(""), 3500)
            return
        }
        await fetchStoreProductsData();
    }

    const handleSearchCashier = async (e) => {
        setCashierSearch(e.target.value)
        if (e.target.value.trim().length > 0)
            await fetchSearchingStoreProductsDataCashier(e.target.value)
        else
            await fetchStoreProductsData()
    }

    const handleSearchUPC = async (e) => {
        setUPCSearch(e.target.value)
        setStoreProductFilter(0)
    };


    function StoreProductForm(props) {
        const {onClose, open, row} = props;
        const [sellingPrice, setSellingPrice] = useState(row ? row.sellingPrice : 1)
        const [productsNumber, setProductsNumber] = useState(row ? row.productsNumber : 0)
        const [product, setProduct] = useState(row ? row.productId : "")
        const [disableAdd, setDisableAdd] = useState(true)

        const productOptions = products.map(product => ({
            label: product.name,
            value: product.id
        }));

        useEffect(() => {
            if(productsNumber && product && sellingPrice >= 1) {
                setDisableAdd(false)
            }else {
                setDisableAdd(true)
            }

        }, [sellingPrice, productsNumber, product]);

        const handleAdd = async () => {
            handleClose()
            let response;
            if(!row) {
                response = await axios.post("http://localhost:8080/api/store-product", {
                    productId: +product,
                    sellingPrice: sellingPrice,
                    productsNumber: productsNumber,
                    promotional: false
                })
            } else {
                response = await axios.put("http://localhost:8080/api/store-product", {
                    id: row.id,
                    sellingPrice: sellingPrice,
                    productsNumber: productsNumber,
                })
            }
            if (response.data.error) {
                setErrorMessage(response.data.error)
                setTimeout(() => setErrorMessage(""), 3500)
            } else {
                fetchStoreProductsData()
            }
        }


        return (
            <Dialog onClose={onClose} open={open}>
                <DialogContent>
                    <FormControl fullWidth>
                        <Autocomplete
                            value={productOptions.find(option => option.value === product)}
                            onChange = {(event, newValue) => {
                                if(newValue)
                                    setProduct(newValue.value)
                                else
                                    setProduct("")
                            }}
                            readOnly={row !== undefined}
                            renderInput={(params) => <StyledTextField {...params} sx={{width: "100%"}} size="medium" label="Product"/>}
                            options={productOptions}
                            sx={{display:"flex"}}
                        />
                        <StyledTextField
                            type="number"
                            InputProps={{ inputProps: { min: 0 } }}
                            onChange={(e) => setProductsNumber(parseInt(e.target.value))}
                            value={productsNumber}
                            label="Number"
                        />
                        <StyledTextField
                            type="number"
                            step="any"
                            InputProps={{ inputProps: { min: 1 } }}
                            onChange={(e) => setSellingPrice(e.target.value)}
                            value={sellingPrice}
                            label="Price"
                        />
                        <StyledButton
                            variant="text"
                            sx={{width: '50%', alignSelf: 'center'}}
                            onClick={handleAdd}
                            disabled={disableAdd}
                        >{row ? "Update" : "Add"}</StyledButton>
                    </FormControl>
                </DialogContent>
            </Dialog>
        )
    }

    function Row(props) {
        const {row} = props;
        const [open, setOpen] = useState(false);

        return (
            <React.Fragment>
                <TableRow sx={{'& > *': {borderBottom: 'unset'}}}>
                    <StyledTableCell>
                        <IconButton
                            aria-label="expand row"
                            size="small"
                            onClick={() => setOpen(!open)}>
                            {open ? <KeyboardArrowUpIcon/> : <KeyboardArrowDownIcon/>}
                        </IconButton>
                    </StyledTableCell>
                    <StyledTableCell component="th" scope="row " align="center">{row.id}</StyledTableCell>
                    <StyledTableCell component="th" scope="row " align="center">{row.name}</StyledTableCell>
                    <StyledTableCell align="center">{row.sellingPrice} ₴</StyledTableCell>
                    <StyledTableCell align="center">{row.productsNumber}</StyledTableCell>
                    <StyledTableCell align="right">
                        <Button disabled={role === "CASHIER"} onClick={() => handlePromotional(row)}>
                            {row.promotional ? <PercentIcon color="info"/> : <PercentIcon color="action"/>}
                        </Button>
                        {!row.promotional && role === "MANAGER" && <Button onClick={() => handleUpdate(row)}>
                            <ModeEditIcon color='action'/>
                        </Button>}
                        {!row.promotional && role === "MANAGER" && <Button onClick={() => handleDelete(row.id)}>
                            <DeleteOutlineOutlinedIcon color="error"/>
                        </Button>}
                    </StyledTableCell>
                </TableRow>
                <TableRow>
                    <StyledTableCell style={{paddingBottom: 0, paddingTop: 0}} colSpan={6}>
                        <Collapse in={open} timeout="auto" unmountOnExit>
                            <Box sx={{margin: 1}}>
                                <Typography variant="span" gutterBottom component="div" style={{fontSize: 18}}>
                                    Characteristics
                                </Typography>
                                {row.characteristics}
                            </Box>
                        </Collapse>
                    </StyledTableCell>
                </TableRow>
            </React.Fragment>
        );
    }

    function StoreProductsTable() {
        let width = role === "MANAGER" ? "17%" : "25%"

        return (
            <React.Fragment>
                <TableContainer component={Card} sx={{ maxWidth: 950, margin: '30px auto', maxHeight: '60vh', overflowY: 'auto' }}>
                    <Table stickyHeader aria-label="collapsible table">
                        <TableHead>
                            <StyledTableRow>
                                <StyledTableCell/>
                                {columns.map((column) => (
                                    <StyledTableCell align="center" key={column.field} sx={{width:{width}}}>
                                        {column.headerName}
                                    </StyledTableCell>
                                ))}
                                <StyledTableCell sx={{width:"25%"}} />
                            </StyledTableRow>
                        </TableHead>
                        <TableBody>
                            {rows.map((row) => (
                                <Row key={row.name} row={row} />
                            ))}
                        </TableBody>
                    </Table>
                </TableContainer>
            </React.Fragment>
        )
    }

    return (
        <Container style={{ marginTop: '50px'}}>
            <StoreProductForm
                open={openForm}
                onClose={handleClose}
                row={updateRow}
            />
            <Box sx={{maxWidth: 950, margin: '0 auto'}}>
                <Stack direction='row' justifyContent='space-between'>
                    {role === 'MANAGER' &&
                        <StyledButton variant="outlined"
                                      startIcon={<AddIcon />}
                                      onClick={handleOpenForm}
                                      sx={{maxHeight:'40px', marginTop:'10px'}}
                        >
                            Add
                        </StyledButton>}
                    <FormControl variant="outlined" size="small" sx={{maxHeight:'40px', minWidth:'120px'}}>
                        <StyledLabel variant="outlined" id="demo-simple-select-label">
                            Store products
                        </StyledLabel>
                        <StyledSelect sx={{maxHeight:'40px', minWidth:'120px'}}
                                      labelId="demo-simple-select-label"
                                      id="demo-simple-select"
                                      label="Store products"
                                      value={storeProductFilter}
                                      onChange={(event) => {setStoreProductFilter(event.target.value); setUPCSearch(""); setCashierSearch("")}}
                        >
                            <MenuItem
                                key={"All"}
                                value={0}
                            >All</MenuItem>
                            <MenuItem
                                key={"Promotional"}
                                value={1}
                            >Promotional</MenuItem>
                            <MenuItem
                                key={"Not promotional"}
                                value={2}
                            >Not promotional</MenuItem>
                        </StyledSelect>
                    </FormControl>
                    {role === "CASHIER" &&
                        <SearchContainer sx={{maxHeight:'40px', marginTop:'10px'}}>
                            <SearchIconWrapper>
                                <SearchIcon />
                            </SearchIconWrapper>
                            <StyledInputBase
                                placeholder="Product…"
                                value={cashierSearch}
                                inputProps={{ 'aria-label': 'search' }}
                                onChange={handleSearchCashier}
                            />
                        </SearchContainer>
                    }
                    <SearchContainer sx={{maxHeight:'40px', marginTop:'10px'}}>
                        <SearchIconWrapper>
                            <SearchIcon />
                        </SearchIconWrapper>
                        <StyledInputBase
                             placeholder="UPC…"
                             value={UPCSearch}
                             inputProps={{ 'aria-label': 'search' }}
                             onChange={handleSearchUPC}
                        />
                    </SearchContainer>
                </Stack>
                {errorMessage && <Alert style={{width: '40%', fontSize: '15px', position: 'fixed', right: '30%', top: '5%'}} severity="error" onClose={clear}>{errorMessage}</Alert>}
                <StoreProductsTable/>
                {role === 'MANAGER' &&
                    <Stack direction='row' justifyContent='center'>
                        <a href="http://localhost:8080/report/STORE_PRODUCTS" target="_blank">
                            <StyledButton variant="outlined" sx={{maxHeight:'40px', marginTop:'10px'}}>
                                PRINT REPORT
                            </StyledButton>
                        </a>
                    </Stack>
                }
            </Box>
        </Container>
    );

}

export default StoreProducts